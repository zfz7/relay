import {Config} from "./types";

export const getConfig: () => Promise<Config> = () =>
  fetch('/api/admin/config', {
    method: "GET",
    headers: {'Content-Type': 'application/json'}
  }).then(response => response.json())

