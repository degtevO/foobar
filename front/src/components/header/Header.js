import React from 'react';
import { Col, Icon, Row, Typography } from 'antd';

import styles from './Header.module.css'
import Avatar from "./avatar/Avatar"
import { withRouter } from "react-router"
import axios from '../../utils/axios'

const pageTitles = {
    '/main': 'Лента',
    '/login': 'Войти',
    '/challenge/search': 'Поиск',
    '/challenge/select': 'Выбор задания',
    '/challenge/take': 'Назначаем задание',
    '/challenge/current': 'Задание',
    '/challenge/give': 'Задание',
    '/settings': 'Настройки'
}


const getTitle = (location) => {
    return pageTitles[location.pathname] || 'Задание'
}


class Header extends React.Component {

    componentDidMount() {
        axios.get('user/info').then((data) => {
            this.setState({
                user: data.data
            })
        })
    }

    state = {
        user: null
    }

    render() {
        return  (
            <header className={styles.header}>
                <Row gutter={16} type="flex" justify="space-around" align="middle" className={styles.user}>
                    <Col span={6}>
                        <Typography.Title level={2} className={styles.title}><a href="/main">Now!</a></Typography.Title>
                    </Col>
                    <Col span={12} >
                        <Typography.Title level={2}>{getTitle(this.props.location)}</Typography.Title>
                    </Col>
                    <Col span={6} style={{ textAlign: 'right' }}>
                        <Avatar user={this.state.user}/>
                    </Col>
                </Row>
            </header>
        )
    }
}


export default withRouter(Header)
