package org.zfz7.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.zfz7.exchange.CodeDTO
import org.zfz7.exchange.ConfigDTO
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

  @PostMapping("/api/admin/code")
  fun updateCode(principal: Principal?, @RequestBody code: CodeDTO) = adminService.updateCode(principal, code.code)

  @GetMapping("/api/admin/code")
  fun getCode(principal: Principal?): CodeDTO = adminService.getCode(principal)

  @GetMapping("/api/admin/config")
  fun getConfig(principal: Principal?): ConfigDTO = adminService.getConfig(principal)

  @PostMapping("/api/admin/config")
  fun updateCode(principal: Principal?, @RequestBody config: ConfigDTO) = adminService.updateConfig(principal, config)
}