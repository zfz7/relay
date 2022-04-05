import React, {useEffect, useState} from 'react'
import {Header} from "./Header";
import {getAdmin} from "../exchange/GetAdmin";
import {AdminDTO} from "../exchange/types";
import {Typography} from "@mui/material";

export const AdminPage: React.FC = () => {
  const [admin, setAdmin] = useState<AdminDTO>()
  useEffect(() => {
    getAdmin().then((dto) => {
      setAdmin(dto)
    })
  }, [])
  return (<>
      <Header/>
      {admin && <Typography> You have {admin.count} users</Typography>}
    </>
  )
}

