import React from "react"
import style from "./ChallengeSearch.module.css"
import { withRouter } from "react-router"


export default withRouter(({ history }) => {

    setTimeout(() => {
        history.push('/challenge/select')
    }, 5000)
    return (
        <div className={style.content}>
            <div className={style.loader}>
                <p className={style.text}>Ищем задания</p>
            </div>
        </div>
    )
})
