package org.usedcar.rest.webservice.api;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usedcar.rest.webservice.assembler.CarAdModelAssembler;
import org.usedcar.rest.webservice.business.CarAdService;
import org.usedcar.rest.webservice.dto.CarAdDTO;
import org.usedcar.rest.webservice.dto.CarSearchDTO;
import org.usedcar.rest.webservice.model.CarAd;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rest/v1/ad")
@Tag(name = "CarAdController", description = "Handles endpoints that are connected to CarAds")
public class CarAdController {

    private final CarAdService carAdService;
    private final CarAdModelAssembler carAdModelAssembler;

    public CarAdController(CarAdService carAdService, CarAdModelAssembler carAdModelAssembler) {
        this.carAdService = carAdService;
        this.carAdModelAssembler = carAdModelAssembler;
    }

    @Operation(summary = "Saving new CarAd")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Saving is finished successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(allOf = {Integer.class}))),
            @ApiResponse(responseCode = "400", description = "Field validations fails, RequestBody is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<Integer> saveCarAd(
            @Parameter(description = "Contains the necessary fields to save a new Car Ad")
            @RequestBody @Valid CarAdDTO carAdDTO) {
        return new ResponseEntity<>(carAdService.saveCarAd(carAdDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Searching among Car Ads")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Searching has at least one result",
            content = @Content(mediaType = "application/json", schema = @Schema(allOf = {List.class, String.class}))),
            @ApiResponse(responseCode = "400", description = "Field validations fails, RequestBody is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Search has no result"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @GetMapping("/search")
    public ResponseEntity<List<String>> searchCarAd(
            @Parameter(description = "Object that contains the necessary fields for searching among Car Ads")
            @RequestBody @Valid CarSearchDTO carSearchDTO) {
        List<CarAd> carAdList = carAdService.searchCarAd(carSearchDTO);
        List<String> urls = carAdList.stream().map(carAd ->
                carAdModelAssembler.toModel(carAd).getRequiredLink("self").getHref()).collect(Collectors.toList());
        return new ResponseEntity<>(urls, HttpStatus.OK);
    }

    @Operation(summary = "Get a Car Ad by its Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Searching has resulted in a Car Ad",
            content = @Content(mediaType = "application/json", schema = @Schema(allOf = {CarAd.class}))),
            @ApiResponse(responseCode = "400", description = "Path-variable is missing"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Search has no result"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public ResponseEntity<CarAd> findCarAdById(
            @Parameter(description = "The ID of Car Ad")
            @PathVariable int id) {
        return new ResponseEntity<>(carAdService.findCarAdById(id), HttpStatus.OK);
    }

    @Operation(summary = "Deleting Car Ad")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Deleting a Car Ad was successful",
            content = @Content()),
            @ApiResponse(responseCode = "400", description = "Path-variable is missing, User try to delete a resource that is not belong to his account"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "The given Car Ad is not exist"
                    , content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Sending request to the endpoint without pre-authentication"
                    , content = @Content(mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarAdById(@Parameter(description = "The ID of Car Ad")
                                                @PathVariable int id) {
        carAdService.deleteCarAd(id);
        return ResponseEntity.noContent().build();
    }
}
