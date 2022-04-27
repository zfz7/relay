import {Code} from "./types";

export const getCode: () => Promise<Code> = () =>
  fetch('/api/admin/code', {
    method: "GET",
    headers: {'Content-Type': 'application/json'}
  }).then(response => response.json())

