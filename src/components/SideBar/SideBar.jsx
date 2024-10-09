import { Box, Button, Drawer, MenuItem, Typography } from "@mui/material";
import * as React from "react";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import InsertChartOutlinedIcon from "@mui/icons-material/InsertChartOutlined";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";
import PermIdentityOutlinedIcon from "@mui/icons-material/PermIdentityOutlined";
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import { useNavigate } from "react-router-dom";

const drawerWidth = 240;

const SideBar = () => {
  const navigate = useNavigate();

  return (
    <Drawer
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        "& .MuiDrawer-paper": {
          width: drawerWidth,
          boxSizing: "border-box",
          color: "white",
        },
        "& .MuiPaper-root": {
          background: "#171B36",
        },
      }}
      variant="permanent"
      anchor="left"
    >
      <Box display={"flex"} justifyContent={"center"} alignContent={"center"}>
        <ListItemButton>
          <ListItemIcon>
              <AddCircleOutlineOutlinedIcon/>
          </ListItemIcon>
          <ListItemText>
            Tạo đơn
          </ListItemText>
        </ListItemButton>
      </Box>
      <Divider />
      <List
        sx={{ width: "100%", maxWidth: 360, bgcolor: "inherit" }}
        component="nav"
      >
        {["Thống kê", "Đơn hàng", "Trang cá nhân"].map((text, index) => (
          <ListItem
            key={text}
            onClick={
              index === 0
                ? () => {
                    navigate("/form");
                  }
                : index === 1
                ? () => {
                    navigate("/");
                  }
                : index === 2
                ? () => {
                    navigate("/login");
                  }
                : undefined
            }
            sx={{ display: "flex" }}
          >
            <ListItemButton>
              <ListItemIcon
                sx={{
                  color: "white",
                  "& . Mui-ListItemIcon-root": {
                    marginTop: "21px",
                  },
                }}
              >
                {index === 0 ? (
                  <InsertChartOutlinedIcon />
                ) : index === 1 ? (
                  <LocalShippingOutlinedIcon />
                ) : index === 2 ? (
                  <PermIdentityOutlinedIcon />
                ) : undefined}
              </ListItemIcon>
              <ListItemText primary={text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      <Divider />
    </Drawer>
  );
};
export default SideBar;
