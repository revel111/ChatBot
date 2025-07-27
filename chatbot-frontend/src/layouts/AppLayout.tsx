import {Layout as AntLayout} from 'antd';
import AppFooter from "../components/AppFooter.tsx";
import {Outlet} from "react-router-dom";
// import {Header} from "antd/es/layout/layout";
// import Footer from './Footer';

const {Content, Footer: AntFooter} = AntLayout;

export default function Layout() {
    return (
        <AntLayout>
            {/*<Header className="bg-background_black flex items-center px-6 shadow-md">*/}
            {/*    <h1 className="text-xl font-semibold text-magnolia">ChatBot</h1>*/}
            {/*</Header>*/}

            {/*<Content className="flex-grow px-6 py-8 bg-night">*/}
            <Content>
                <Outlet/>
            </Content>

            <AntFooter>
                <AppFooter/>
            </AntFooter>
        </AntLayout>
    );
}
