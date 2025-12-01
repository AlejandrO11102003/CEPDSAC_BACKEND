package com.example.cepsacbackend.mapper;

import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.model.Pago;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    @Mapping(target = "metodoPagoDescripcion", source = "metodoPago.descripcion")
    @Mapping(target = "tipoMetodo", source = "metodoPago.tipoMetodo")
    @Mapping(target = "nombreAlumno", source = "matricula.alumno.nombre")
    @Mapping(target = "emailAlumno", source = "matricula.alumno.correo")
    @Mapping(target = "nombreCurso", expression = "java(pago.getMatricula().getProgramacionCurso().getCursoDiplomado() != null ? pago.getMatricula().getProgramacionCurso().getCursoDiplomado().getTitulo() : \"\")")
    @Mapping(target = "nombreMetodoPago", source = "metodoPago.descripcion")
    PagoResponseDTO toResponseDTO(Pago pago);

    List<PagoResponseDTO> toResponseDTOList(List<Pago> pagos);
}
