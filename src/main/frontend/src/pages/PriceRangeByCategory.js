import React, {useEffect, useState} from 'react';
import axios from "axios";

const PriceRangeByCategory = () => {
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [minPriceInfo, setMinPriceInfo] = useState([]);
    const [maxPriceInfo, setMaxPriceInfo] = useState([]);

    useEffect(() => {
        axios.get(`api/categories`)
            .then(response => {
                if (response.status === 200 && response.data.resultCode === 200) {
                    setCategories(response.data.result);

                    if (response.data.result.length > 0) {
                        setSelectedCategory(response.data.result[0].categoryName); // Adjust as necessary
                    }
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

    useEffect(() => {
        if (!selectedCategory) return;

        axios.get(`/api/categories/${selectedCategory}/price-range`)
            .then((response) => {
                if (response.status === 200 && response.data.resultCode === 200) {
                    setMinPriceInfo(response.data.result["최저가"]);
                    setMaxPriceInfo(response.data.result["최고가"]);
                } else {
                    const msg = `(${response.data.resultCode}) ${response.data.resultMsg}`;
                    alert(msg);
                    throw new Error(msg);
                }
            })
            .catch((err) => {
                console.error(err);
            });
    }, [selectedCategory]);

    const categoryChange = (event) => {
        setSelectedCategory(event.target.value);
    };

    return (
        <div>
            <h2>카테고리별 최저, 최고 가격 브랜드 및 상품</h2>
            <select className="form-select form-select-sm" aria-label=".form-select-sm example" style={{width: '150px'}}
                    onChange={categoryChange} value={selectedCategory}>
                {categories.map((category) => (
                    <option value={category.categoryName}>
                        {category.categoryName}
                    </option>
                ))}
            </select>
            {minPriceInfo.length > 0 && maxPriceInfo.length > 0 && (
                <div>
                    <table className="table">
                        <thead>
                        <tr>
                            <th>&nbsp;</th>
                            <th>브랜드</th>
                            <th>가격</th>
                        </tr>
                        </thead>
                        <tbody>
                            {minPriceInfo.map((item) => (
                                <tr>
                                    <td><strong>최저가</strong></td>
                                    <td>{item.브랜드}</td>
                                    <td>{item.가격}</td>
                                </tr>
                            ))}
                            {maxPriceInfo.map((item) => (
                                <tr>
                                    <td><strong>최고가</strong></td>
                                    <td>{item.브랜드}</td>
                                    <td>{item.가격}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default PriceRangeByCategory;