import { Outlet, Link } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  CssBaseline,
  Box,
  Drawer,
} from "@mui/material";

const drawerWidth = 180;

const Layout = () => {
  return (
    <Box sx={{ display: "flex", height: "100vh", overflow: "hidden" }}>
      <CssBaseline />

      {/* Sidebar */}
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: drawerWidth,
            boxSizing: "border-box",
          },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: "auto", p: 2 }}>
          <Link to="/">Home</Link>
          <br />
          <Link to="/about">About</Link>
          <br />
          <Link to="/form">Create order</Link>
          <br />
          <Link to="/user">Profile</Link>
          <br />
          <Link to="/about">About</Link>
          <br />
          <Link to="/about">About</Link>
        </Box>
      </Drawer>

      {/* Main Content */}
      <Box sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        {/* Navbar */}
        <AppBar
          position="fixed"
          sx={{
            width: `calc(100% - ${drawerWidth}px)`,
            ml: `${drawerWidth}px`,
          }}
        >
          <Toolbar>
            <Typography variant="h6" noWrap>
              My Navbar
            </Typography>
          </Toolbar>
        </AppBar>

        {/* Toolbar for spacing adjustment */}
        <Toolbar />

        {/* Content rendered via Outlet */}
        <Box
          sx={{
            flexGrow: 1,
            p: 3,
            overflow: "auto",
            bgcolor: "#eeeeee",
            display: "flex", // Ensure the Box itself is a flex container
            flexDirection: "column", // Set direction if you have child components
          }}
        >
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default Layout;
