import React, { useEffect, useRef, useState } from 'react';
import { useStyles } from './Style.js';
import { Tabs, Tab, Box } from '@material-ui/core';

import Header from '../Header/Header';
import ClientPage from "./Pages/Client";
import DevicePage from "../Administrator/Pages/Device";
import axiosInstance from '../../axios.js';
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";

const Administrator = () => {

    const classes = useStyles();

    const [tab, setTab] = useState("client")
    const [clientList, setClientList] = useState([])
    const [deviceList, setDeviceList] = useState([])
    const [anchorEl, setAnchorEl] = useState(null);
    const [isAdmin, setIsAdmin] = useState(false)

    const ws = useRef(null);
    const [isPaused, setPause] = useState(false);

    const changePage = (page) => {
        setTab(page)
    }

    useEffect(() => {
        console.log(sessionStorage.getItem("role"))
        setIsAdmin(sessionStorage.getItem("role"))
        console.log(isAdmin)
    }, [isAdmin])

    useEffect(() => getRepo(), [])
    const getRepo = () => {
        axiosInstance.get("/client")
            .then((res) => {
                const clientList = res.data;
                setClientList(clientList)
            })
            .catch((err) => {
                console.log(err);
            })
        axiosInstance.get("/device")
            .then((res) => {
                const deviceList = res.data;
                setDeviceList(deviceList)
            })
            .catch((err) => {
                console.log(err);
            })
    }

    return (
        <div>
            {isAdmin == "true" &&
                (<Box><Header userType="Administrator" />
                    <Tabs indicatorColor="primary" className={classes.tabs} value={tab}>
                        <Tab label="Clients" onClick={() => changePage("client")} value="client" />
                        <Tab label="Devices" onClick={() => changePage("device")} value="device" />
                    </Tabs>

                    {tab == "client" &&
                        <ClientPage clientList={clientList} />
                    }
                    {tab == "device" &&
                        <DevicePage deviceList={deviceList} />
                    }

                </Box>
                )}
        </div>
    )
}


export default Administrator;