import { makeStyles } from "@material-ui/core";

export const useStyles = makeStyles (() => ({ 
    header:{
        display:"flex",
        alignItems:"center",
        justifyContent:"space-between",
        height:40,
        padding:"10px 20px",
        background:'linear-gradient(45deg, #daf1f1 30%, #46b9b9 90%)'
    }
}));