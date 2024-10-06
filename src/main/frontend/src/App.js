import React from 'react';
import {BrowserRouter as Router, Route, Routes, Link, Navigate} from 'react-router-dom';
import './App.css';
import LowestPrice from "./pages/LowestPrice";
import LowestPriceOfBrands from "./pages/LowestPriceOfBrands";
import PriceRangeByCategory from "./pages/PriceRangeByCategory";

function App() {
  return (
      <Router>
        <div className="app-container">
          <nav className="sidebar">
            <ul>
              <li>
                <Link to="/lowest-price">카테고리별 최저가</Link>
              </li>
              <li>
                <Link to="/lowest-price/brand">최저가 브랜드 내 카테고리별 최저가</Link>
              </li>
              <li>
                <Link to="/price-range">카테고리별 최저/최고가</Link>
              </li>
            </ul>
          </nav>
          <div className="content">
            <Routes>
              <Route path="/" element={<Navigate to="/lowest-price" />} />
              <Route path="lowest-price" element={<LowestPrice />} />
              <Route path="/lowest-price/brand" element={<LowestPriceOfBrands />} />
              <Route path="/price-range" element={<PriceRangeByCategory />} />
            </Routes>
          </div>
        </div>
      </Router>
  );
}

export default App;
