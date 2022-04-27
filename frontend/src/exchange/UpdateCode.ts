export const updateCode: (code: string) => Promise<void> = (code: string) =>
  fetch('/api/admin/code',{
    method: "POST",
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({code})
  }).then()

