import React from "react"
import Challenge from "./challenge/Challenge"
import _ from 'lodash'

import axios from '../../utils/axios'


class ChallengeSelect extends React.Component {

    state = {
        daily: null,
        assigned: null,
        types: null
    }

    componentDidMount() {
        axios.get('challengeTypes?limit=150').then(({ data }) => {
            this.setState({
                types: data
            })
        })

        axios.get('challenge/assigned').then(({ data }) => {
            this.setState({
                assigned: data
            })
        })

        axios.post('challenge/random').then(({ data }) => {
            this.setState({
                daily: data
            })
        })
    }

    mergeWithType = (challenge, types, isDaily) => {
        const type = _.find(types, { id: challenge.typeId }) || {}
        const name = isDaily ? 'Задание дня' : challenge.creator === 0 ? type.title : challenge.creator
        return {
            ...challenge,
            ...type,
            id: challenge.id,
            name,
            description: `${type.title} ${type.description}` || '-'
        }
    }

    render() {

        const { daily, assigned, types } = this.state

        const dailyChallenge = daily && types ? this.mergeWithType(daily, types, true) : void 0
        const assignedChallenges = assigned && types ? assigned.map(ass => this.mergeWithType(ass, types)) : void 0

        return (
            <React.Fragment>

                {dailyChallenge &&
                <Challenge key={dailyChallenge.name} challenge={dailyChallenge} type="daily"/>
                }
                {assignedChallenges && assignedChallenges.map(challenge => (
                    <Challenge key={challenge.name} challenge={challenge}/>
                ))}
            </React.Fragment>
        );
    }
}

export default ChallengeSelect
