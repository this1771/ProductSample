import React, { useEffect, useState } from 'react';
import axios from "axios";

const LowestPrice = () => {
    const [products, setProduct] = useState([]);
    const [totalPrice, setTotalPrice] = useState("0");

    useEffect(() => {
        axios.get("/api/lowest-price")
            .then((response) => {
                if (response.status === 200 && response.data.resultCode === 200) {
                    setProduct(response.data.result["최저가"]["카테고리"]);
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
            <h2>카테고리별 최저가</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>카테고리</th>
                        <th>브랜드</th>
                        <th>가격</th>
                    </tr>
                </thead>
                <tbody>
                {
                    products.map((item) => {
                        return (
                        <tr>
                            <td>{item.카테고리}</td>
                            <td>{item.브랜드}</td>
                            <td>{item.가격}</td>
                        </tr>
                        );
                    })
                }
                <tr>
                    <td colSpan={2}><strong>총액</strong></td>
                    <td><strong>{totalPrice}</strong></td>
                </tr>
                </tbody>
            </table>
        </div>
    );
};

export default LowestPrice;