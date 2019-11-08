import { Avatar, Card } from "antd"
import style from "./Friend.module.css"
import { Typography } from "antd"
import React from "react"
import { withRouter } from "react-router"


const giveChallenge = (history, id) => () => {
    history.push(`/challenge/give/${id}`)
}

export default withRouter(({ history, friend }) => (
    <Card bordered={true} bodyStyle={{ display: 'flex' }} className={style.card} onClick={giveChallenge(history, friend.id)}>
        <Avatar size="small" src={friend.avatar} icon="user" />
        <Typography.Text className={style.username}>{friend.name}</Typography.Text>
    </Card>
))
