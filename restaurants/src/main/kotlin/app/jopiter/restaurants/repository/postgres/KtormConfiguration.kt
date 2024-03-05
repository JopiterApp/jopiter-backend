package app.jopiter.restaurants.repository.postgres

import org.ktorm.database.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class KtormConfiguration(
  private val datasource: DataSource
) {
  @Bean
  fun database() = Database.connectWithSpringSupport(datasource)
}
