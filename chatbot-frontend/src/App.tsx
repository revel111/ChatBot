import {RouterProvider} from "react-router-dom";
import Router from "./configs/Router.tsx";

export default function App() {
    return (
        <RouterProvider router={Router()}/>
    )
}