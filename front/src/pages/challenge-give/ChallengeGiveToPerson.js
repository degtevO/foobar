import React from "react"
import _ from 'lodash'

import style from './ChangeGiveToPerson.module.css'
import { Avatar, Button, Radio, Typography, Select } from "antd"
import axios from "../../utils/axios"
import { withRouter } from "react-router"

const Option = Select.Option;

class ChallengeGiveToPerson extends React.Component {

    state = {
        user: null,
        challenges: []
    }

    componentDidMount() {

        // console.log(this.props)
        const userId = this.props.match.params.id

        axios.get('user').then(({ data }) => {
            // debugger
            // const user = _.find(data, { id: userId}) || {}

            this.setState({
                user: {
                    avatar: "https://randomuser.me/api/portraits/women/12.jpg",
                    name: 'Айгуль',
                    id: userId
                }
            })
        })

        axios.get('challengeTypes?limit=150').then(({ data }) => {
            this.setState({ challenges: data })
        })
    }

    handleChange = (selectedChallenge) => {
        this.setState({
            selectedChallenge
        })
    }

    assign = () => {
        axios.post(`challenge/${this.state.selectedChallenge}/assign/${this.props.match.params.id}`).then(() => {
            this.props.history.push('/main')
        })
    }


    render() {

        const { user, challenges, selectedChallenge } = this.state
        if (!user) {
            return null
        }

        return (
            <div className={style.content}>
                <Avatar shape="square" size={64}
                        src={user.avatar}/>
                <Typography.Text>{user.name}</Typography.Text>
                <br/>
                <div>
                    <Select size={'lg'} onChange={this.handleChange} style={{ width: 200 }}>
                        {challenges.map(c => (
                            <Option value={c.id}>{c.title}</Option>
                        ))}
                    </Select>
                </div>
                <br/>
                <br/>
                <Button disabled={!selectedChallenge} type={"primary"} shape="round" size={'large'} icon="exclamation" onClick={this.assign}>Выдать</Button>
            </div>
        )
    }
}

export default withRouter(ChallengeGiveToPerson)
