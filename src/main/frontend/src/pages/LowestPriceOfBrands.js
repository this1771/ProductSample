import React, { useEffect, useState } from 'react';
import axios from "axios";

const LowestPriceOfBrands = () => {
    const [products, setProduct] = useState([]);
    const [brandName, setBrandName] = useState("");
    const [totalPrice, setTotalPrice] = useState("0");

    useEffect(() => {
        axios.get("/api/lowest-price/brand")
            .then((response) => {
                if (response.status === 200 && response.data.resultCode === 200) {
                    setProduct(response.data.result["최저가"]["카테고리"]);
                    setBrandName(response.data.result["최저가"]["브랜드"]);
                    setTotalPrice(response.data.result["최저가"]["총액"]);
                } else {
                    const msg = `(${response.data.resultCode}) ${response.data.resultMsg}`;
                    alert(msg);
                    throw new Error(msg);
                }
            })
            .catch((err) => {
                console.error(err);
            });
    }, []);

    return (
        <div>
            <h2>최저가 브랜드 내 카테고리별 최저가</h2>
            <br/>
            <div>
                <h4>
                    · 최저가 브랜드 :
                    <span style={{color: '#0000FF'}}>{brandName}</span>
                </h4>
            </div>
            <table className="table">
                <thead>
                <tr>
                    <th>카테고리</th>
                    <th>가격</th>
                </tr>
                </thead>
                <tbody>
                {
                    products.map((item) => {
                        return (
                            <tr>
                                <td>{item.카테고리}</td>
                                <td>{item.가격}</td>
                            </tr>
                        );
                    })
                }
                <tr>
                    <td><strong>총액</strong></td>
                    <td><strong>{totalPrice}</strong></td>
                </tr>
                </tbody>
            </table>
        </div>
    );
};

export default LowestPriceOfBrands;