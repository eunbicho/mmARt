package com.ssafy.mmart

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MmartApplication

fun main(args: Array<String>) {
	runApplication<MmartApplication>(*args)
}
