package com.algaworks.algafood.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Locale;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

    private String valorFieled;
    private String descricaoField;
    private String descricaoObrigatoria;

    @Override
    public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
       this.valorFieled = constraintAnnotation.valorFieled();
       this.descricaoField = constraintAnnotation.descricaoField();
       this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
    }

    @Override
    public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
        boolean valido = true;

        try {
            BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorFieled)
                    .getReadMethod().invoke(objetoValidacao);

            String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField)
                    .getReadMethod().invoke(objetoValidacao);

            if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
                valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
            }
            return valido;

        } catch (Exception e) {
            throw new ValidationException(e);
        }

    }
}
