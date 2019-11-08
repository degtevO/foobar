import React from "react"
import { Button, Typography } from "antd"
import Friend from "./Friend"
import axios from '../../../utils/axios'

import styles from './Friends.module.css'
import { withRouter } from "react-router"

//FIXME: С бэка
const friends = [
    {
        name: 'Олег Дегтев',
        avatar: '',
        lastCompletedChallenge: {
            title: 'Спас галактику!',
            date: '25.04.2019'
        },
        badges: [
            1
        ]
    },
    {
        name: 'Костя Лобов',
        avatar: '',
        lastCompletedChallenge: {
            title: 'Победил Таноса',
            date: '23.04.2019'
        }
    },
    {
        name: 'Тони Старк',
        avatar: '',
        lastCompletedChallenge: {
            title: 'Щёлкнул пальцем как боженька',
            date: '22.04.2019'
        }
    },
    {
        name: 'Саян Базарсадаев',
        avatar: '',
        lastCompletedChallenge: {
            title: 'Полежал на диване',
            date: '21.04.2019'
        },
        badges: [
            1
        ]
    },
]

const giveChallenge = (history) => () => {
    history.push('/challenge/give')
}


class Friends extends React.Component {

    state = {
        feed: []
    }

    componentDidMount() {
        axios.get('challenge/feed').then(({ data }) => {
            console.log(data)
        })
    }

    render() {

        if (this.state.feed.length === 0) {
            // return null
        }

        return (
            <React.Fragment>
                <div className={styles.header}>
                    <Typography.Title level={4} className={styles.title}>Друзья</Typography.Title>
                    <Button type="primary" className={styles.button} onClick={giveChallenge(this.props.history)}>
                        Дать задание
                    </Button>
                </div>
                {friends.map(friend => (
                    <Friend key={friend.name} friend={friend}/>
                ))}
                <div className={styles.footer}>
                    <Button type="primary" className={styles.button} onClick={giveChallenge(this.props.history)}>
                        Дать задание
                    </Button>
                </div>
            </React.Fragment>
        )
    }
}

export default withRouter(Friends)
