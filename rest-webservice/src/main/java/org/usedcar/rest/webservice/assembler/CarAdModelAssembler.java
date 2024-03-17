package org.usedcar.rest.webservice.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.usedcar.rest.webservice.api.CarAdController;
import org.usedcar.rest.webservice.model.CarAd;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarAdModelAssembler implements RepresentationModelAssembler<CarAd, EntityModel<CarAd>> {


    @Override
    public EntityModel<CarAd> toModel(CarAd entity) {
        return EntityModel.of(entity, linkTo(methodOn(CarAdController.class).findCarAdById(entity.getId())).withSelfRel());
    }
}
