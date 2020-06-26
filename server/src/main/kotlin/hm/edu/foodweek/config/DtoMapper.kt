package hm.edu.foodweek.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DtoMapper {

    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper()
    }
}