import React from "react"
import { Spin, Tabs } from "antd"
import Friend from "./Friend"
import axios from "../../utils/axios"


import style from './ChangeGiveToPerson.module.css'

const { TabPane } = Tabs

export default class ChallengeGive extends React.Component {

    state = {
        tab: "1",
        // friends: [],
        // nearby: [],
        all: []
    }

    loadUsers = () => {

        axios.get('user').then(({ data }) => {
            this.setState({ all: data })
        })

    }

    componentWillMount() {
        this.loadUsers()
    }

    changeTab = (tab) => {
        this.setState({ tab })
    }

    render() {

        if (this.state.all.length === 0) {
            return (<div className={style.loader}><Spin style={{ display: 'flex', justifyContent: 'center' }} size="large"/></div>)
        }
        return (
            <React.Fragment>
                <Tabs style={{ textAlign: 'center' }} defaultActiveKey="1" onChange={this.changeTab}>
                    <TabPane tab="Друзья" key="1">
                        {this.state.all.map(friend => (
                            <Friend key={friend.name} friend={friend}/>
                        ))}
                    </TabPane>
                    <TabPane tab="Рядом" key="2">
                        {this.state.all.map(friend => (
                            <Friend key={friend.name} friend={friend}/>
                        ))}
                    </TabPane>
                    <TabPane tab="Все" key="3">
                        {this.state.all.map(friend => (
                            <Friend key={friend.name} friend={friend}/>
                        ))}
                    </TabPane>
                </Tabs>
            </React.Fragment>
        )
    }
}
