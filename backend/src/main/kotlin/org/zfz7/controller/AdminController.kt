package org.zfz7.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.zfz7.exchange.AdminDTO
import org.zfz7.service.AdminService


@RestController
class AdminController(
  val adminService: AdminService
) {
  @GetMapping("/api/admin")
  fun getAdmin(): AdminDTO = adminService.getAdminPage()
}