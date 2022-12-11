package com.example.demo.controller

import com.example.demo.service.UserService
import com.example.demo.service.ValuableService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import java.util.Base64
import javax.servlet.http.HttpServletRequest

operator fun HttpServletRequest.get(name: String): Any? = session.getAttribute(name)
operator fun HttpServletRequest.set(name: String, value: Any) = session.setAttribute(name, value)
val HttpServletRequest.isDarkAttribute get() = session.getAttribute("isDark") as Boolean? ?: false

@Controller
class RestController(
    private val valuableService: ValuableService,
    private val userService: UserService
) {
    private val String.valuable get() = this == "valuable"
    private val String.user get() = this == "user"
    private val Any.ok get() = ResponseEntity<Unit>(HttpStatus.OK)
    private val Any.badRequest get() = ResponseEntity<Unit>(HttpStatus.BAD_REQUEST)

    // curl 'localhost:8082/insertOrUpdate/valuable' -X POST -H 'Content-type: application/json' -d '{"name":"a@","description":"aa@@","cost":0,"id":7}'
    // curl 'localhost:8082/insertOrUpdate/user' -X POST -H 'Content-type: application/json' -d '{"_name":"a","_password":"aa","id":null}'
    @PreAuthorize("permitAll()")
    @RequestMapping("/insertOrUpdate/{entity}", method = [RequestMethod.POST, RequestMethod.PUT])
    fun insertOrUpdate(@PathVariable entity: String, @RequestBody json: String) =
        (if (entity.valuable) valuableService += Json.decodeFromString(json)
        else if (entity.user) userService += Json.decodeFromString(json)
        else throw IllegalArgumentException()).ok

    // curl 'localhost:8082/get/valuable?id=1'
    // curl 'localhost:8082/get/user?id=7'
    @PreAuthorize("permitAll()")
    @ResponseBody
    @GetMapping("/get/{entity}")
    fun get(@PathVariable entity: String, @RequestParam id: Int) =
        if (entity.valuable) Json.encodeToString(valuableService[id])
        else if (entity.user) Json.encodeToString(userService[id])
        else throw IllegalArgumentException()

    // curl 'localhost:8082/delete/valuable?id=7' -X DELETE
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete/{entity}")
    fun delete(@PathVariable entity: String, @RequestParam id: Int) =
        (if (entity.valuable) valuableService -= id
        else if (entity.user) userService -= id
        else throw IllegalArgumentException()).ok

    // curl 'localhost:8080/isDark' -c cookie.txt -b cookie.txt
    @PreAuthorize("permitAll()")
    @ResponseBody
    @GetMapping("/isDark")
    fun isDark(request: HttpServletRequest) = request.isDarkAttribute

    // curl 'localhost:8080/setDark?isDark=true' -X POST -c cookie.txt -b cookie.txt
    @PreAuthorize("permitAll()")
    @PostMapping("/setDark")
    fun setDark(request: HttpServletRequest, @RequestParam isDark: Boolean)
    = run { request["isDark"] = isDark }.ok

    // curl 'localhost:8080/changeTheme' -X POST -c cookie.txt -b cookie.txt
    @PreAuthorize("permitAll()")
    @PostMapping("/changeTheme")
    fun changeTheme(request: HttpServletRequest)
    = run { request["isDark"] = !request.isDarkAttribute }.ok

    // curl 'localhost:8080/getLogin' -c cookie.txt -b cookie.txt
    @PreAuthorize("permitAll()")
    @ResponseBody
    @GetMapping("/getLogin")
    fun getLogin(request: HttpServletRequest)
    = request["login"] as String? ?: false

    // curl 'localhost:8080/setLogin?login=dfhgjklreabhgrfdvgjh' -X POST -c cookie.txt -b cookie.txt
    @PreAuthorize("permitAll()")
    @PostMapping("/setLogin")
    fun setLogin(request: HttpServletRequest, @RequestParam login: String)
    = run { request["login"] = login }.ok

    @PreAuthorize("permitAll()")
    @PostMapping("/upload")
    fun fileUpload(@RequestParam file: MultipartFile, request: HttpServletRequest) = run {
        val content = file.bytes
        var bool = true

        for ((j, i) in "%PDF".withIndex())
            bool = bool and (content[j] == i.code.toByte())
        if (!bool) return@run badRequest

        request["file"] = file.originalFilename to Base64.getEncoder().encodeToString(content)
        ok
    }

    @PreAuthorize("permitAll()")
    @Suppress("UNCHECKED_CAST")
    @GetMapping("/download")
    fun fileDownload(request: HttpServletRequest) = request["file"]!!.run {
        this as Pair<String, String>?
        val content = Base64.getDecoder().decode(second)

        ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=\"$first\"")
            .header("Content-Description", "File Transfer")
            .header("Cache-Control", "no-cache, no-store, must-revalidate")
            .header("Pragma", "no-cache")
            .header("Expires", "0")
            .contentLength(content.size.toLong())
            .body(ByteArrayResource(content))
    }
}
