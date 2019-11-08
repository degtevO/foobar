import { Avatar, Card, Icon, Typography } from "antd"
import React from "react"
import style from './Friend.module.css'

const Header = ({ avatar, name, badges = [] }) => (
    <div>
        <Avatar size="small" icon="user" />
        {badges.length > 0 && <Icon type="star" style={{ color: 'gold', marginLeft: 8 }} />}
        <Typography.Text className={style.text}>{name}</Typography.Text>
    </div>
)

export default ({ friend }) => (
    <Card title={<Header {...friend}/>} bordered={true} bodyStyle={{ display: 'flex' }} className={style.card}>
        <Typography.Text className={style.date}>{friend.lastCompletedChallenge.date}</Typography.Text>
        <Typography.Text>{friend.lastCompletedChallenge.title}</Typography.Text>
    </Card>
)
