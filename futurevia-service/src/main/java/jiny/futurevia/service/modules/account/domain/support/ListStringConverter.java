package jiny.futurevia.service.modules.account.domain.support;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {
	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		return Optional.ofNullable(attribute)
			.filter(list -> !list.isEmpty())
			.map(a -> String.join(",", a))
			.orElse(null);
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		return Stream.of(dbData.split(","))
			.collect(Collectors.toList());
	}
}