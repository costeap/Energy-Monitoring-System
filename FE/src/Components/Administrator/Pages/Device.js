import React, { useEffect, useState } from 'react';
import { useStyles } from '../Style.js';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tooltip, Menu, MenuItem, Paper, Button, Dialog, DialogTitle, DialogContent, TextField, Typography } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import CloseIcon from '@material-ui/icons/Close';
import axiosInstance from "../../../axios";
import { Alert, AlertTitle } from '@material-ui/lab';
import WebSocketListenerInstance from '../../Ws/WebSocketListener.js';
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";


const DevicePage = ({ deviceList }) => {

    const classes = useStyles();

    const [anchorEl, setAnchorEl] = useState(null);
    const [isAddModalOpen, setIsAddModalOpen] = useState();
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [deviceDataset, setDeviceDataset] = useState();
    const [isClientViewOpen, setIsClientViewOpen] = useState(false);
    const [clientDevice, setClientDevice] = useState([]);

    //for insert device
    const [descriptionDevice, setDescriptionDevice] = useState(0);
    const [addressDevice, setAddressDevice] = useState(0);
    const [maximumHourlyEnergyConsumptionDevice, setMaximumHourlyEnergyConsumptionDevice] = useState(0);

    //for update device
    const [descriptionDeviceUpdate, setDescriptionDeviceUpdate] = useState(0);
    const [addressDeviceUpdate, setAddressDeviceUpdate] = useState(0);
    const [maximumHourlyEnergyConsumptionDeviceUpdate, setMaximumHourlyEnergyConsumptionDeviceUpdate] = useState(0);
    const [clientIdDeviceUpdate, setclientIdDeviceUpdate] = useState(0);

    const onMenuClick = (event, device) => {
        event.stopPropagation();
        setDeviceDataset(device);
        setAnchorEl(event.currentTarget);
    };

    const onMenuClose = (e) => {
        e.stopPropagation();
        setAnchorEl(null);
    };

    const onAddDevice = () => {
        setIsAddModalOpen(true);
    }

    const onAddDeviceClose = () => {
        setIsAddModalOpen(false);
    }

    const onEditDevice = () => {
        setIsEditModalOpen(true);
        onMenuClose();
    }

    const onEditClose = () => {
        setIsEditModalOpen(false);
    }

    const onViewClient = () => {
        getClient();
        setIsClientViewOpen(true);
        onMenuClose()
    }

    const onCloseClient = () => {
        setIsClientViewOpen(false);
    }

    const onDeleteDevice = () => {
        axiosInstance.delete("/device/del/" + deviceDataset.id)
            .then(
                res => {

                    <Alert severity="success">
                        <AlertTitle>Success</AlertTitle>
                        Client deleted
                    </Alert>
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const addNewDevice = () => {
        let credentilas = {
            description: descriptionDevice,
            address: addressDevice,
            maximumHourlyEnergyConsumption: maximumHourlyEnergyConsumptionDevice,
        }

        axiosInstance.post("/device", credentilas)
            .then(
                res => {
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const getClient = () => {
        axiosInstance.get("/device/client/" + deviceDataset.id)
            .then((res) => {
                console.log("salut")
                console.log(res.data)
                const clientDevice = res.data;
                setClientDevice(clientDevice)
            })
            .catch((err) => {
                console.log(err);
            })
    }

    const editDevice = () => {
        let credentilas = {
            description: descriptionDeviceUpdate,
            address: addressDeviceUpdate,
            maximumHourlyEnergyConsumption: maximumHourlyEnergyConsumptionDeviceUpdate,
            clientID: clientIdDeviceUpdate,
        }

        axiosInstance.put("/device/" + deviceDataset.id, credentilas)
            .then(
                res => {
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const generateClient = () => {
        return (
            deviceDataset && (clientDevice.id != null) ? (
                <TableContainer component={Paper}>
                    <Table className={classes.dialogTable} aria-label="customized table">
                        <TableHead className={classes.tableHead}>
                            <TableRow className={classes.tableRow}>
                                <TableCell className={classes.tableCell}>ID</TableCell>
                                <TableCell className={classes.tableCell}>Name</TableCell>
                                <TableCell className={classes.tableCell}>Username</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody className={classes.tableBody}>


                            <TableRow className={classes.tableRow} key={clientDevice.id}>
                                <TableCell className={classes.tableCell} component="th" scope="row">
                                    {clientDevice.id}
                                </TableCell>
                                <TableCell className={classes.tableCell}> {clientDevice.name}</TableCell>
                                <TableCell className={classes.tableCell}> {clientDevice.username}</TableCell>
                            </TableRow >


                        </TableBody>
                    </Table>

                </TableContainer>) : (<Typography>No client</Typography>)
        )
    }

    return (
        <div>
            <div className={classes.buttonContainer}>
                <Button className={classes.addButton} onClick={onAddDevice}>Add new device</Button>
            </div>
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="customized table">
                    <TableHead className={classes.tableHead}>
                        <TableRow className={classes.tableRow}>
                            <TableCell className={classes.tableCell}>ID</TableCell>
                            <TableCell className={classes.tableCell}>Description</TableCell>
                            <TableCell className={classes.tableCell}>Address</TableCell>
                            <TableCell className={classes.tableCell}>Maximum Hourly Energy Consumption</TableCell>
                            <TableCell className={classes.buttonSpace}></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody className={classes.tableBody}>
                        {deviceList.map((device) => (<>
                            <TableRow className={classes.tableRow} key={device.id}>
                                <TableCell className={classes.tableCell} component="th" scope="row">
                                    {device.id}
                                </TableCell>
                                <TableCell className={classes.tableCell}> {device.description}</TableCell>
                                <TableCell className={classes.tableCell}> {device.address}</TableCell>
                                <TableCell className={classes.tableCell}> {device.maximumHourlyEnergyConsumption}</TableCell>
                                <TableCell className={classes.menuButton}>
                                    <Tooltip title="Options" arrow placement="right">
                                        <Button onClick={(e) => onMenuClick(e, device)}>
                                            <MenuIcon />
                                        </Button>

                                    </Tooltip>
                                </TableCell>
                            </TableRow >
                        </>
                        ))}
                        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={onMenuClose}>
                            <MenuItem onClick={onDeleteDevice}>Delete Device</MenuItem>
                            <MenuItem onClick={onEditDevice}>Edit Device</MenuItem>
                            <MenuItem onClick={onViewClient}>View client</MenuItem>
                        </Menu>
                    </TableBody>
                </Table>

            </TableContainer>

            <Dialog open={isAddModalOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">Add Device</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onAddDeviceClose} />
                    </div>

                </DialogTitle>
                <DialogContent >
                    <div className={classes.section}>
                        <TextField onChange={e => setDescriptionDevice(e.target.value)} label="Description" variant="filled" />
                        <TextField onChange={e => setAddressDevice(e.target.value)} label="Address" variant="filled" />
                    </div>
                    <div className={classes.section}>
                        <TextField onChange={e => setMaximumHourlyEnergyConsumptionDevice(e.target.value)} label="Maximum Hourly Energy Consumption" variant="filled" />
                    </div>
                    <div className={classes.container}>
                        <Button className={classes.button} onClick={addNewDevice}>Add</Button>
                    </div>
                </DialogContent>
            </Dialog>

            <Dialog open={isClientViewOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">Client</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onCloseClient} />
                    </div>

                </DialogTitle>
                <DialogContent >
                    {generateClient()}
                </DialogContent>
            </Dialog>

            {deviceDataset &&
                <Dialog open={isEditModalOpen}>
                    <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                        <div className={classes.dialogTitle}>
                            <Typography variant="subtitle1">Edit</Typography>
                            <CloseIcon style={{ cursor: "pointer" }} onClick={onEditClose} />
                        </div>

                    </DialogTitle>
                    <DialogContent>
                        <div className={classes.section}>
                            <TextField onChange={e => setDescriptionDeviceUpdate(e.target.value)} label="Description" variant="filled" />
                            <TextField onChange={e => setAddressDeviceUpdate(e.target.value)} label="Address" variant="filled" />
                        </div>
                        <div className={classes.section}>
                            <TextField onChange={e => setMaximumHourlyEnergyConsumptionDeviceUpdate(e.target.value)} label="Maximum Hourly Energy Consumption" variant="filled" />
                            <TextField onChange={e => setclientIdDeviceUpdate(e.target.value)} label="Client" variant="filled" />
                        </div>
                        <div className={classes.container}>
                            <Button className={classes.button} onClick={editDevice}>Save</Button>
                        </div>
                    </DialogContent>
                </Dialog>

            }
        </div>
    )

}

export default DevicePage;