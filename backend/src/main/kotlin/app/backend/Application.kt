package app.backend

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
@Controller
class Application {


    @GetMapping
    fun start(): String = "index.html"

    // fill db with some data on startup
//    @Bean
//    fun fillDB(repository: ToDoRepository) = CommandLineRunner {
//        repository.save(ToDoEntity(text = "making fritz2 great"))
//        repository.save(ToDoEntity(text = "fixed as much bugs as possible"))
//        repository.save(ToDoEntity(text = "build some new examples with fritz2"))
//        repository.save(ToDoEntity(text = "produce better code"))
//    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}