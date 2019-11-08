import React from "react"
import { Affix, Button } from "antd"

import style from './Main.module.css'
import Events from "./events/Events"
import Friends from "./friends/Friends"
import DoItButton from "./DoItButton"
import { withRouter } from "react-router"
import axios from '../../utils/axios'

class Main extends React.Component {

    state = {
        hasActiveChallenge: false
    }

    componentDidMount() {
        axios.get('challenge/accepted').then(({ data }) => {
            if (data.length > 0) {
                this.setState({ hasActiveChallenge: true })
            }
        })
    }

    goToChallenge = () => {
        this.props.history.push(`/challenge/current`)
    }

    render() {
        return (
            <React.Fragment>
                <div className={style.content}>
                    {this.state.hasActiveChallenge &&
                        <div className={style.challenge}>
                            <Button type="default" onClick={this.goToChallenge}>Ваше активное задание</Button>
                        </div>
                    }
                    <Events/>
                    <Friends/>
                </div>
                <Affix offsetBottom={32} className={style["action-bar"]}>
                    <DoItButton />
                </Affix>
            </React.Fragment>
        )
    }
}

export default withRouter(Main)
