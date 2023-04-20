FROM node:latest as builder
COPY . /app
WORKDIR /app
ENV PATH="./node_modules/.bin:$PATH"
COPY . .
RUN npm run build 
CMD ["npm", "start"]
