package com.nexusbank.mapper;

import com.nexusbank.dto.TrunkDTO;
import com.nexusbank.model.Trunk;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrunkMapper {
    TrunkMapper INSTANCE = Mappers.getMapper(TrunkMapper.class);

    Trunk toModel(TrunkDTO trunkDTO);

    TrunkDTO toDto(Trunk trunk);
}
