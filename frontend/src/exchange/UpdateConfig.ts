import {Config} from "./types";

export const updateConfig: (config: Config) => Promise<void> = (config: Config) =>
  fetch('/api/admin/config',{
    method: "POST",
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(config)
  }).then()

