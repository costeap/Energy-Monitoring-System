import { makeStyles } from "@material-ui/core";

export const useStyles = makeStyles (() => ({ 
    tabs:{
        boxShadow: "5px 0 5px rgb(0 0 0 / 20%)",
    },
    table:{
        margin:"40px auto", 
        maxWidth: "70% !important",
    },
    tableUsersActivity:{
        margin:"60px auto", 
        maxWidth: "120% !important",
    },
    tableHead:{
        backgroundColor:"#daf1f1"
    },
    tableBody:{
        fontSize:14
    },
    tableRow:{
        "&:nth-child(even)":{
            backgroundColor:"lightGrey"
        }
    },
    tableCell:{
        maxWidth:50
    },
    tableCellDialog:{
        maxWidth:500
    },
    dialogTitle:{
        display:"flex",
        justifyContent:"space-between",
        alignItems:"center",
        minWidth:500
    },
    dialogTable:{
        margin:"0 auto",
    },
    section:{
        display:"flex",
        justifyContent:"space-around",
        alignItems:"center",
        marginBottom:10
    },
    addButton:{
        backgroundColor:"#daf1f1",
        alignItems:"right",
    },
    markAsReadButton:{
        backgroundColor:"#46b9b9"
    },
    buttonContainer:{
        margin:"auto",
        maxWidth:"fit-content",
        marginTop:20
    },
    container: {
        margin: "auto",
        maxWidth: "fit-content",
        marginTop: 8
    },
    textArea:{
        display:"flex",
        justifyContent:"space-between",
        alignItems:"center",
        minWidth:500,
        minHeight:300
    },
    textField:{
        display:"flex",
        justifyContent:"space-between",
        alignItems:"left",
        minWidth:300
    },
}));