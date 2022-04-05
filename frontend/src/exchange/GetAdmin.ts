import {AdminDTO} from "./types";

export const getAdmin: () => Promise<AdminDTO> = () => {
  return fetch('/api/admin', {
    method: "GET",
    headers: {'Content-Type': 'application/json'}
  }).then(response => response.json())
}
