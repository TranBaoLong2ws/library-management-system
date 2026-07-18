import React from 'react';
import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { HomePage } from './layouts/Homepage/HomePage';
import { SearchBooksPage } from './layouts/SearchBookPage/SearchBookPage';
import { Redirect, Route, Router, Switch, useHistory } from 'react-router-dom';
import { BookCheckoutPage } from './layouts/BookCheckOutPage/BookCheckoutPage';

import { auth0Config } from './lib/auth0Config';
import LoginPage from './Auth/LoginPage';
import { Auth0Provider, withAuthenticationRequired } from '@auth0/auth0-react';
import { ReviewListPage } from './layouts/BookCheckOutPage/ReviewListPage/ReviewListPage';
import { ShelfPage } from './layouts/ShelfPage/ShelfPage';
import { MessagesPage } from './layouts/Message/MessagesPage';
import { ManageLibbraryPage } from './layouts/MangeLibraryPage/ManageLibraryPage';
import { PaymentPage } from './layouts/PaymentPage/PaymentPage';


const Auth0ProviderWithHistory = ({ children }: { children: React.ReactNode }) => {
  const history = useHistory();

  const onRedirectCallback = (appState: any) => {
    history.push(appState?.returnTo || "/home");
  };

  return (
    <Auth0Provider
      domain={auth0Config.domain}
      clientId={auth0Config.clientId}
      authorizationParams={{
        redirect_uri: auth0Config.redirectUri,
        audience: auth0Config.audience,
        scope: auth0Config.scope,
      }}
      onRedirectCallback={onRedirectCallback}
    >
      {children}
    </Auth0Provider>
  );
};

const SecureRoute = ({ component, path, ...args }: { component: React.ComponentType<any>, path: string }) => (
  <Route path={path} component={withAuthenticationRequired(component)} {...args} />
);




export const App = () => {
  return (
    <div className='d-flex flex-column min-vh-100'>
      <Auth0ProviderWithHistory>
        <Navbar />
        <div className='flex-grow-1'>
          <Switch>

            <Route path="/" exact>
              <Redirect to="/home"></Redirect>
            </Route>

            <Route path="/home">
              <HomePage />
            </Route>

            <Route path="/search">
              <SearchBooksPage />
            </Route>

            <Route path={'/reviewlist/:bookId'}>
              <ReviewListPage />
            </Route>

            <Route path="/checkout/:bookId">
              <BookCheckoutPage />
            </Route>

            <Route path='/login' render={() => <LoginPage />} />
            <SecureRoute path='/shelf' component={ShelfPage} />
            <SecureRoute path='/messages' component={MessagesPage} />
            <SecureRoute path='/admin' component={ManageLibbraryPage} />
            <SecureRoute path='/fees' component={PaymentPage} />

          </Switch>
        </div>

        <Footer />
      </Auth0ProviderWithHistory>
    </div>
  );
}


