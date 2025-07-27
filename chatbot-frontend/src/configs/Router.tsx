import {createBrowserRouter} from "react-router-dom";
import AppLayout from "../layouts/AppLayout";
import LandingPage from "../features/LandingPage.tsx";

export default function Router() {
    return createBrowserRouter([
        {
            path: '/',
            element: <AppLayout/>,
            children : [
                {
                    index: true,
                    element: <LandingPage/>
                }
            ]
        }
    ]);
}