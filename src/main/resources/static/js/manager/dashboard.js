document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    // dung du lieu cung neu ko co du lieu that
    const labels = window.chartLabels || ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
    const data = window.chartData || [3000, 2500, 3200, 4100, 2800, 3900, 3000, 2000, 2800, 1900, 2400, 2900];

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu',
                data: data,
                backgroundColor: '#0056b3',
                borderRadius: 4,
                maxBarThickness: 40
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false // Hide legend to match design
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed.y !== null) {
                                label += new Intl.NumberFormat('vi-VN').format(context.parsed.y) + ' đ';
                            }
                            return label;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return new Intl.NumberFormat('vi-VN').format(value) + ' đ';
                        },
                        color: '#6c757d',
                        font: {
                            family: 'Inter',
                            size: 12
                        }
                    },
                    border: {
                        display: false
                    },
                    grid: {
                        color: '#dee2e6',
                        drawTicks: false,
                        borderDash: [5, 5] // Dashed lines for the grid
                    }
                },
                x: {
                    grid: {
                        display: false,
                        drawBorder: false
                    },
                    ticks: {
                        color: '#6c757d',
                        font: {
                            family: 'Inter',
                            size: 12
                        }
                    },
                    border: {
                        display: false
                    }
                }
            }
        }
    });
});
