import {Suspense, useState} from 'react';
import ReactDOM from 'react-dom/client';
import {BrowserRouter} from 'react-router-dom';
import {HelmetProvider} from 'react-helmet-async';

import {EventType, PublicClientApplication} from '@azure/msal-browser';

import {msalConfig} from './components/auth/authConfig';


import App from './app';
import {DevSupport} from "@react-buddy/ide-toolbox";
import {ComponentPreviews, useInitial} from "./dev";

// ----------------------------------------------------------------------

const msalInstance = new PublicClientApplication(msalConfig);

// Default to using the first account if no account is active on page load
if (!msalInstance.getActiveAccount() && msalInstance.getAllAccounts().length > 0) {
    // Account selection logic is app dependent. Adjust as needed for different use cases.
    msalInstance.setActiveAccount(msalInstance.getActiveAccount()[0]); // TODO - error handling if account is diff
}

// Listen for sign-in event and set active account
msalInstance.addEventCallback((event) => {
    if (event.eventType === EventType.LOGIN_SUCCESS && event.payload.account) {
        const account = event.payload.account;
        msalInstance.setActiveAccount(account);
    }
});

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <HelmetProvider>
        <BrowserRouter>
            <Suspense>
                <DevSupport ComponentPreviews={ComponentPreviews}
                            useInitialHook={useInitial}
                >
                    <App instance={msalInstance} />
                </DevSupport>
            </Suspense>
        </BrowserRouter>
    </HelmetProvider>
);
