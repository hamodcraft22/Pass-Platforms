# Use nginx-alpine as the parent image
FROM nginx:alpine

# Copy html-javascript output files to nginx
COPY ./build /usr/share/nginx/html

# Make port 80 available to the world outside this container
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]