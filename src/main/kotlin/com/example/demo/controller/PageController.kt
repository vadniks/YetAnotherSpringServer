package com.example.demo.controller

import com.example.demo.service.GraphService
import com.example.demo.service.UserService
import com.example.demo.service.ValuableService
import com.example.demo.unit
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@Controller
class PageController(
    private val valuableService: ValuableService,
    private val userService: UserService,
    private val graphService: GraphService
) {

    private operator fun Model.plusAssign(nameAndAttribute: Pair<String, Any>)
    = addAttribute(nameAndAttribute.first, nameAndAttribute.second).unit

    @PreAuthorize("permitAll()")
    @GetMapping("/catalogue")
    fun catalogue(model: Model, request: HttpServletRequest): String {
        model += "valuables" to valuableService.all
        model += "_isDark" to request.isDarkAttribute
        return "catalogue"
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/view")
    fun view(model: Model, @RequestParam id: Int, request: HttpServletRequest): String {
        model += "valuable" to valuableService[id]!!
        model += "_isDark" to request.isDarkAttribute
        return "view"
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin")
    fun admin(model: Model, request: HttpServletRequest): String {
        model += "users" to userService.all
        model += "_isDark" to request.isDarkAttribute
        return "admin"
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/pdf")
    fun pdf(model: Model, request: HttpServletRequest)
    = "pdf".also { model += "_isDark" to request.isDarkAttribute }

    @PreAuthorize("permitAll()")
    @GetMapping("/stats")
    fun stats(model: Model, request: HttpServletRequest) = "stats".also {
        model += "_isDark" to request.isDarkAttribute
        for (i in 1..3) model += "graph$i" to graphService[i]
    }
}
