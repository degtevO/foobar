import { Avatar, Col, Icon, Row, Spin, Typography } from "antd"

import styles from "../Header.module.css"
import React from "react"

export default ({ user }) => (
    <Col gutter={16} type="flex" direction="" justify="space-around" align="middle">
        {!user ? <Spin size="small" /> :

            <React.Fragment>

                <Row span={6}>
                    <Typography.Text className={styles.karma}>
                        +{user.karma}
                    </Typography.Text>
                </Row>
                <Row span={6}>
                    <Avatar src={user.avatar} className={styles.avatar}/>
                </Row>
                <Row span={12} className={styles.badges}>
                    <Icon type="star" style={{ color: 'gold' }}/>
                    <Icon type="star" style={{ color: 'gold' }}/>
                </Row>
            </React.Fragment>
        }
    </Col>
)
