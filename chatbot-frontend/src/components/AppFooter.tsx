import {GithubOutlined, LinkedinOutlined} from '@ant-design/icons';

export default function AppFooter() {
    return (
        <footer
            className="bg-background_black text-magnolia py-6 px-4 flex flex-col md:flex-row justify-between items-center">
            <div className="flex space-x-4 mt-4 md:mt-0">
                <a
                    href="https://github.com/revel111"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-magnolia hover:text-onyx transition-colors duration-200"
                >
                    <GithubOutlined style={{fontSize: '20px'}}/>
                </a>
                <a
                    href="https://www.linkedin.com/in/arturarshava/"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-magnolia hover:text-onyx transition-colors duration-200"
                >
                    <LinkedinOutlined style={{fontSize: '20px'}}/>
                </a>
            </div>
            <div className="text-sm">
                ChatBot 2025 — All rights reserved
            </div>
        </footer>
    );
}