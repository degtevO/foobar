import { Typography, Button, Card } from "antd"
import style from "../Main.module.css"
import coublet from "./coublet.svg"
import React from "react"


export default () => (
    <React.Fragment>
        <Typography.Title level={4}>События</Typography.Title>

        <Card bordered={true} className={style.event} bodyStyle={{ display: 'flex' }}>
            <img alt="coublet" className={style["event-logo"]} src={coublet}/>
            <div className={style['event-text']}>
                <p>Марафон добрых дел</p>
                <p>25.06.19 - 25.07.19</p>
                <Button size="small" shape="round">Погнали!</Button>
            </div>
        </Card>
    </React.Fragment>
)
