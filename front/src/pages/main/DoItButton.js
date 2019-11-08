import { Button } from "antd"
import React from "react"

import style from './DoItButton.module.css'
import { withRouter } from "react-router"

const handleDoItClick = (history) => () => history.push('/challenge/search')

export default withRouter(({ history }) => (
    <Button className={style.button} type="primary" shape="round" size={'large'} icon="exclamation" onClick={handleDoItClick(history)}>
        Взять задание
    </Button>
))
