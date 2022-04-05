package org.zfz7.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
  @GetMapping(value = ["/"])
  fun index() = "forward:index.html"

  @GetMapping(value = ["{path:[^.]*}","/*/{path:[^.]*}"])
  fun forward() = "forward:/"

  @GetMapping(value = ["/admin"])
  fun forwardAdmin() = "forward:/"
}
