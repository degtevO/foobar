import React from "react"
import { Link } from 'react-router-dom';

import style from './IndexPage.module.css';

let videoParams = 'autoplay=1';
videoParams += '&showinfo=0';
videoParams += '&modestbranding=1';

export default () => (
    <div className={style.content}>

        <div className={style.greyBlock}>
            <div className={style.propmoBlock}>
                <p className={style.introText}>Вас приветствует</p>
                <p className={style.introText}>сервис добрых дел</p>
                <p className={style.introText}><span className={style.logo}>NOW!</span></p>

                <iframe
                    title="propmo"
                    type="text/html"
                    width="330"
                    height="187"
                    allowfullscreen="allowfullscreen"
                    src={`http://www.youtube.com/embed/crhn668ona4?${videoParams || ''}`}
                    frameBorder="0"
                />
                <Link className={style.skip} to={'/login'}>пропустить</Link>
            </div>
        </div>
    </div>
)
