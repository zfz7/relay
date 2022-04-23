package org.zfz7.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.zfz7.exchange.Logs
import org.zfz7.exchange.Peers
import org.zfz7.service.AdminService
import java.security.Principal


@RestController
class AdminController(
  val adminService: AdminService
) {
  @GetMapping("/api/admin/peers")
  fun getPeers(principal: Principal?): Peers = adminService.getAdminPage(principal)

  @GetMapping("/api/admin/logs")
  fun getLogs(principal: Principal?): Logs = adminService.getLogs(principal)
}