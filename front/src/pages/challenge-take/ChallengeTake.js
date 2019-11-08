import { withRouter } from "react-router"
import axios from '../../utils/axios'
import { Spin } from "antd"
import React from "react"


export default withRouter(({ history, match }) => {
    axios.put(`challenge/${match.params.id}/accept`)
        .then(() => {
            history.push('/challenge/current')
        })

    return <Spin size="large" />
})
