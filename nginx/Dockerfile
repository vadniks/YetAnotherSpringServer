FROM nginx
WORKDIR /usr/share/nginx/html-static
COPY default.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
EXPOSE 8082
