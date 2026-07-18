import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { App } from './App';
import { BrowserRouter } from 'react-router-dom';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';

const stripePromise = loadStripe('pk_test_51Tng4QAqRG8YjHOq8YEdpCGmL0PCX1sejPnhCwhzeHNDT0jY4Q28kOG8YJRRkmUTdvIDujRI16aF5xRfvDGw1npJ00Y1C90ecp');

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(

  <BrowserRouter>
    <Elements stripe={stripePromise}>
      <App />
    </Elements>
  </BrowserRouter>

);

